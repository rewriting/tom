package sl

// This file ports the canonical strategy-builder helpers from
// tom.library.sl.* (the `tom_make_*` static methods scattered across
// the engine plugins). Each function returns a strategy graph
// expressed in terms of [Mu]/[MuVar]/[Choice]/[ChoiceId]/[All]/[One]/
// [Sequence]/[SequenceId]/[OneId]/[Identity], byte-for-byte equivalent
// to the Java definitions.
//
// Naming follows Go style (Make* exported, lower-cased var names) but
// the algebra is unchanged; downstream plugins port directly with a
// search-and-replace from `tom_make_X(v)` to `sl.MakeX(v)`.

// MakeTry returns a strategy that applies s; on failure, returns the
// subject unchanged.
//
//	Try(s) = Choice(s, Identity)
func MakeTry(s Strategy) Strategy {
	return NewChoice(s, NewChoice(NewIdentity(), nil))
}

// MakeRepeat returns a strategy that applies s repeatedly until it
// fails, then succeeds with the last produced subject. Fails-as-
// identity: a strategy that never makes progress short-circuits via
// the inner [Choice].
//
//	Repeat(s) = Mu(_x, Choice(Sequence(s, _x), Identity))
func MakeRepeat(s Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewChoice(
			NewSequence(s, NewSequence(x, nil)),
			NewChoice(NewIdentity(), nil),
		),
	)
}

// MakeRepeatId is the "identity-aware" sibling of [MakeRepeat]:
// continues *only* while the subject keeps changing.
//
//	RepeatId(s) = Mu(_x, SequenceId(s, _x))
func MakeRepeatId(s Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewSequenceId(s, NewSequenceId(x, nil)),
	)
}

// MakeTopDown applies v at every node from the root down, in a
// pre-order traversal. v must succeed on every visited node (use
// [MakeTry] to wrap if needed).
//
//	TopDown(v) = Mu(_x, Sequence(v, All(_x)))
func MakeTopDown(v Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewSequence(v, NewSequence(NewAll(x), nil)),
	)
}

// MakeTopDownIdStopOnSuccess is the workhorse walker behind the
// generated %strategy code: top-down traversal that stops descending
// into a subtree as soon as v changes its subject. Subjects v leaves
// unchanged are explored recursively (via the All branch).
//
// Used by Desugarer/Typer/etc. to apply a one-shot rewrite rule.
//
//	TopDownIdStopOnSuccess(v) = Mu(x, ChoiceId(v, All(x)))
func MakeTopDownIdStopOnSuccess(v Strategy) Strategy {
	x := NewMuVar("x")
	return NewMu(x,
		NewChoiceId(v, NewChoiceId(NewAll(x), nil)),
	)
}

// MakeOnceTopDown applies v to exactly one node in the tree (top-down,
// left-to-right), then stops. Fails if v fails on every node.
//
//	OnceTopDown(v) = Mu(_x, Choice(v, One(_x)))
func MakeOnceTopDown(v Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewChoice(v, NewChoice(NewOne(x), nil)),
	)
}

// MakeOnceTopDownId is the identity-aware variant: applies v top-down
// and stops at the first node that *changes*.
//
//	OnceTopDownId(v) = Mu(_x, ChoiceId(v, OneId(_x)))
func MakeOnceTopDownId(v Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewChoiceId(v, NewChoiceId(NewOneId(x), nil)),
	)
}

// MakeBottomUp applies v at every node from the leaves up. Mirror of
// [MakeTopDown]: every visit must succeed. Not used by the current
// downstream plugins but listed in the Java built-in set, so we ship
// it here for completeness.
//
//	BottomUp(v) = Mu(_x, Sequence(All(_x), v))
func MakeBottomUp(v Strategy) Strategy {
	x := NewMuVar("_x")
	return NewMu(x,
		NewSequence(NewAll(x), NewSequence(v, nil)),
	)
}
