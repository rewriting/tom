# Go target — Tom standard mappings

This directory mirrors `mapping/java/` but with Go-syntax hook
bodies. Each `.tom` file declares the `%typeterm` and `%op`
operators that let Tom programs talk about primitive/library
types portably (Tom source uses `String`, `int`, `Strategy`,
`BottomUp`, etc. — the mapping resolves each one to its
concrete Go counterpart at compile time).

## Layout

```
go/
  int.tom        — int    ↔ Go `int`
  boolean.tom    — bool   ↔ Go `bool` (Tom uses `boolean`)
  string.tom     — String ↔ Go `string`
  Strategy.tom   — Strategy ↔ `sl.Strategy` interface
  sl.tom         — top-level include: Strategy + basic + composed
  sl/
    basic.tom    — primitive combinators (Mu, MuVar, Identity, Fail,
                   All, One, OneId, Sequence, SequenceId, Choice,
                   ChoiceId).
    composed.tom — composed walks (BottomUp, TopDown, RepeatId, …)
                   defined via backquote expansion of the
                   primitives — same algebra as Java's
                   `mapping/java/sl/composed.tom`.
```

## Differences from `mapping/java/`

Java mappings can express `instanceof X` and `new X(...)` directly.
Go has neither — its inline type-assertion syntax (`v, ok := t.(*X)`)
is a *statement*, but `is_fsym` hooks compile to a single
*expression*. The mappings therefore route `is_fsym` to the
per-type `IsX` helpers in `stable/library/sl/typecheck.go`:

```tom
%op Strategy Mu(...) {
  is_fsym(t) { sl.IsMu($t) }    // ← sl helper, not `$t instanceof`
  make(var, v) { sl.NewMu($var, $v) }
}
```

`Sequence` / `Choice` / `SequenceId` / `ChoiceId` are list-shape
combinators (`%oplist`); their `make_insert` hooks dispatch to
`sl.NewSequence(head, tail)` etc., which already handle the
`tail == nil` terminator case (matching the Java
`Sequence.make(head, tail)` static method's null-shortcut).

## Coverage

Only the subset the current Go fixtures exercise is ported. To
add a missing combinator (e.g. `Omega`, `IfThenElse`, `ChoiceUndet`,
`Not`, `Up`, `Pselect`), copy the Java signature from
`mapping/java/sl/basic.tom` and adapt the `is_fsym` / `make` /
`get_slot` bodies to call the corresponding Go runtime.
