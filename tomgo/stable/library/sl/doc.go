// Package sl is the Go port of tom.library.sl — a strategy/visitor
// library inspired by Stratego and JJTraveler. It is the runtime the
// generated %strategy / %visit constructs of Tom rely on, and the
// substrate that lets each engine plugin (Desugarer, Typer, …) walk and
// rewrite an AST without hand-rolling tree traversal each time.
//
// The design follows the Java reference (stable/tom/library/sl/) closely
// enough that plugin code can be ported nearly line-for-line. Key
// differences:
//
//   - Errors via Go's [error] return rather than checked exceptions. A
//     plain [ErrVisitFailure] sentinel replaces VisitFailure; combinators
//     like [Choice] consult [errors.Is(err, ErrVisitFailure)] to decide
//     whether to recover.
//   - Strategies operate via the [Strategy.VisitLight] method only. The
//     Environment-based [Strategy.Visit] flavour of the Java API is not
//     yet ported — every downstream plugin uses VisitLight exclusively.
//   - The traversal is decoupled from the visited type via the
//     [Introspector] interface, exactly as in the Java library. Callers
//     supply an Introspector for their AST; we ship one for tomast
//     downstream (see [tom/tomgo/stable/library/sl/tomastsl]).
//
// Subset ported (Phases 6.0 → 6.2):
//
//	core    — Strategy, Visitable, Introspector, VisitFailure, Environment
//	leaf    — Identity, Fail
//	combine — Sequence, Choice, All, One, SequenceId, ChoiceId, OneId
//	recur   — Mu, MuVar
//
// Phases 6.3+ add AbstractStrategyBasic (the helper user-defined
// visitors extend) and the canonical built-in walks (TopDown, BottomUp,
// Repeat, TopDownIdStopOnSuccess, …).
package sl
