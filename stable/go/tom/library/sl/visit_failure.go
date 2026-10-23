package sl

import "errors"

// ErrVisitFailure is the sentinel returned by a [Strategy] to signal a
// controlled, recoverable traversal failure. Combinators that branch
// on failure (e.g. [Choice]) check it with [errors.Is]; anything else
// (a wrapped or unrelated error) propagates up unchanged. This is the
// Go analogue of Java's VisitFailure checked exception.
//
// Use [Fail] or [FailWith] to raise it; use [errors.Is(err,
// ErrVisitFailure)] to recover.
var ErrVisitFailure = errors.New("sl: visit failure")
