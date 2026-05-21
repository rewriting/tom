// Package aterm is a Go port of the CWI ATerm library
// (cwi-swat/aterms/aterm-java), simplified by stripping all
// thread-safety constructs. The original Java reference is preserved
// in the repository's stable/lib/runtime/aterm.jar.
//
// The package implements the term family that the historical Tom
// compiler relied on:
//
//   - ATermInt / ATermLong   — integer/long literals
//   - ATermReal              — floating-point literals
//   - ATermAppl              — function application (f(arg1, ..., argN))
//   - ATermList              — head/tail list, plus an empty list
//   - ATermPlaceholder       — <type> hole used by pattern matching
//   - AFun                   — function symbol (name + arity + quoted-flag)
//
// All terms are hash-consed by [Factory] (backed by
// [sharedobjects.Factory] which is itself single-threaded). Two
// structurally equal terms always end up as the same Go pointer, so
// `==` is equivalent to `.IsEqual()`.
//
// What this port covers vs. the Java original:
//
//   - parse(...) / make(pattern, args) / match(pattern) on text inputs
//   - canonical textual writer (Term.String()) + ReadFromTextFile /
//     WriteToTextFile for IO round-trips
//   - annotations are fully implemented on ATermAppl
//     (SetAnnotation / GetAnnotation / RemoveAnnotation) and exposed
//     through the parser's trailing `{...}` syntax. Other term
//     types still return an empty annotation list — Test1 / Test2
//     only exercise annotations on appls
//   - list mutators (Remove / Replace / DictPut / DictGet /
//     DictRemove / RemoveElementAt / Reverse)
//
// Out of scope by design:
//
//   - binary serialisation (BAF / .taf) — Java parses these via
//     aterm.pure.binary; we use TRM text format only
//   - the streaming/blob types (ATermBlob, stream/*) and the
//     Visitor / Visitable infrastructure
package aterm
