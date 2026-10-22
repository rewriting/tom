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
//
//   - Two ways to run a strategy, as in Java. [Strategy.VisitLight]
//     (Java visitLight) passes the subject from strategy to strategy.
//     [Strategy.Visit] (Java visit(T, Introspector)) walks the subject
//     with an [Environment] (Java Environment): every strategy of the
//     tree shares it ([Init]), reads and writes the current subject
//     through it ([Strategy.VisitEnv], Java int visit(Introspector),
//     returning [StatusSuccess] or [StatusFailure]) and can ask for its
//     [Position] (Java Position, with [Path] and [Omega]).
//     [Strategy.VisitWith] is Java visit(Environment, Introspector).
//
//   - No virtual dispatch: a strategy defined outside this package
//     embeds [AbstractStrategy] (or [AbstractStrategyBasic]) and
//     defines VisitLight plus
//
//     func (s *S) VisitEnv(i sl.Introspector) int { return sl.VisitBasic(s, i) }
//     func (s *S) Visit(t any, i sl.Introspector) (any, error) { return sl.VisitStrategy(s, t, i) }
//     func (s *S) VisitWith(e *sl.Environment, i sl.Introspector) (any, error) { return sl.VisitStrategyWith(s, e, i) }
//
//     which is what Tom generates for a Go %strategy, and Gom for its
//     congruence (`_f`, Go VisitF) and predicate (`Is_f`, Go IsF)
//     strategies.
//
//   - In the host code of a %strategy rule compiled with `tom -go`, the
//     receiver is `tom__strat`: Java's getEnvironment() is written
//     `tom__strat.GetEnvironment()` (e.g.
//     `tom__strat.GetEnvironment().GetPosition()`). As in Java, it
//     panics ("environment not initialized") when the strategy is run
//     with VisitLight; under Visit, a rule that does not apply runs the
//     `extends` strategy at the current position of the environment.
//
//   - The traversal is decoupled from the visited type via the
//     [Introspector] interface, exactly as in the Java library. Callers
//     supply an Introspector for their AST; we ship one for tomast
//     downstream (see [tom/tomgo/stable/library/sl/tomastsl]).
//
// Subset ported (Phases 6.0 → 6.2):
//
//	core    — Strategy, Visitable, Introspector, VisitFailure, Environment,
//	          Position, Path
//	leaf    — Identity, Fail
//	combine — Sequence, Choice, All, One, SequenceId, ChoiceId, OneId, Omega
//	recur   — Mu, MuVar
//
// Phases 6.3+ add AbstractStrategyBasic (the helper user-defined
// visitors extend) and the canonical built-in walks (TopDown, BottomUp,
// Repeat, TopDownIdStopOnSuccess, …).
package sl
