# Overnight session — 2026-05-22

Two phases of work landed.

## Phase A — tomgo autonomy + Java backend wrapper for tomgo gom

Commit `da3a8837d`.

- Bundled jars at `stable/lib/{runtime,tom,tools,compiletime,config}/`
  (~2.9 MB total). Everything tomgo needs to expand `%gom` blocks
  for the Java target lives inside this repo. No more reliance on
  `/tom/stable/dist/lib` or `$TOM_HOME`.
- Parser's `expandGomBlock` (Java path) was rewritten to drive the
  bundled `gom.jar` via direct `java -cp <bundled> tom.gom.Gom`,
  using a new `locateBundledGom` / `locateJava` pair that walks up
  from CWD for the marker `stable/lib/tom/gom.jar`. The legacy
  `locateGomBin` (which looked at `/tom/src/dist/bin/gom`) was
  removed.
- `tomgo gom` CLI gained `--target go|java`:
  - `go` (default): in-process Go backend, unchanged.
  - `java`: wraps the bundled gom.jar so callers have a unified
    interface. Same output as legacy `/tom` gom — it IS the legacy
    gom.jar, just hosted from inside the tomgo tree.
- Test harness `resolveResources` points at `tomgo/stable/lib`
  instead of `/tom/stable/dist/lib`. The whole harness runs with
  `TOM_HOME` unset and zero references to paths outside tomgo.

Future direction the user proposed: replace the bundled gom.jar
with a native Go Java-AST emitter. ~500-1000 LOC port. Out of
scope for this overnight session.

## Phase B — fixture porting

Commit `7ecbce633`: **TestNonVarSubjects.t** ported (Java + Go).
Exercises `%match(list(a()))` with a backquote-built subject
(non-variable) on a 6-alt `Term` sort + 1-alt `TermList`. Confirms
the compiler's renamesubjects pass copes with built terms as
subjects.

Status: **10 Java + 10 Go fixtures** end-to-end green.

### Deferred fixtures (real backend gaps)

These fixtures were attempted and reverted; each surfaced one or
more features we don't yet support. Notes for follow-up:

- **TestSublists.t** — Sublist-range matching: pattern
  `list(_*, list(a(),b(),c()), _*)` on subject `list(a(),b(),c())`
  expects the inner `list(...)` to match a CONTIGUOUS RANGE of
  elements, not a single element. Special semantics for variadic
  operators whose pattern includes the same-named variadic head.
  Backend work: a new "list slice" matching mode in the variadic
  propagator/compiler.

- **TestNumericConditions.t** — Two gaps:
  (a) Integer constants emitted as `6()` in BQAppl args (parser
      treats numeric literal as nullary call to the alt named "6").
      Likely parser-side fix in `parseBQTerm`'s constant branch.
  (b) `pat && y > 5` constraint-action form: the numeric expression
      (`>`, `>=`, `<`, `<=`, `==`, `!=`) is wrapped in
      `ConstraintToExpression` which the Java backend's expression
      renderer doesn't handle. Need a case in
      `stable/tom/backend_java/backend_ir.go` for
      `ConstraintToExpressionExpression`.

- **TestAntiPattern2.t** — Record patterns with EMPTY slot list
  (`id[]` vs `id()`) interacting with anti-patterns + annotations
  (`_f@!id[]`). The parser path through `MakeMatchConstraint`
  nil-panics. Needs investigation in the constraint propagation.

- **TestAntiPattern3.t** — Same `MakeMatchConstraint` nil panic
  on heavy anti-pattern coverage (`plus(!x,!y)`, `plus(x,!suc(x))`,
  outer `!plus(x,!suc(y))`). Probably the same root cause as
  TestAntiPattern2.

### Subject-type inference (typer gap)

Multiple fixtures from /tom/test/ use `%match(varname)` without
a sort annotation. Our typer doesn't infer the sort from the
local variable declaration above the match. Workaround: change
to `%match(Sort varname)` — works on all attempted fixtures so
far (TestVarVarStar, TestSublists, TestAntiPattern2).

Proper fix: scan the host-language water before the match for
`<Sort> <name> = ...;` patterns, build a local-name → sort map,
and seed `MatchConstraint.AstType` from it.

## What to look at next

In order of utility:

1. **Native Java AST emitter** in tomgo's gom — would shave the
   ~2.5 MB of bundled jars and give tomgo a 100%-Go Java target.
2. **Numeric constraints** (`ConstraintToExpressionExpression` +
   numeric literals in BQAppl args). Unlocks
   TestNumericConditions and likely many more fixtures.
3. **Subject-type inference** in the typer. Cheap fix that would
   make several /tom/test/ fixtures port without the
   `%match(Sort var)` rewrite.
4. **Sublist-range matching** in the variadic propagator. Bigger
   compiler work; unlocks TestSublists and related.
5. More fixtures from /tom/test/ that don't trip on the above
   (TestAntiPattern itself, TestArrayNonLinear, TestList2, …).
