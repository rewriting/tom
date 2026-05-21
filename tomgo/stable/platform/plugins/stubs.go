package plugins

import "tom/tomgo/stable/platform"

// This file collects the not-yet-implemented engine plugins as
// **identity** Run-throughs. Each one has a deliberate stub that
// lets the Platform chain compile and run end-to-end even when only
// the Parser is fully implemented. As each plugin lands, replace
// the stub with the real implementation in a dedicated file (e.g.
// typer.go, expander.go, …) and delete the stub here.
//
// Per the Java reference order (BootstrapPluginsList.java):
//
//   1. Starter         (starter.go, real)
//   2. Parser          (parser.go,  real)
//   3. Transformer     (stub)
//   4. SyntaxChecker   (stub)
//   5. Desugarer       (stub)
//   6. Typer           (stub)
//   7. TypeChecker     (stub)
//   8. Expander        (stub)
//   9. Compiler        (stub)
//  10. Optimizer       (stub)
//  11. Backend         (stub)

// Transformer is now a real plugin — see transformer.go.

// SyntaxChecker is now a real plugin — see syntaxchecker.go.

// TypeChecker validates the type-resolved AST: every subterm sits in
// its operator's declared domain, every constraint is well-typed.
type TypeChecker struct{}

func (TypeChecker) Name() string                                 { return "TypeChecker" }
func (TypeChecker) Run(in platform.State) (platform.State, error) { return in, nil }

// Expander expands %match / %strategy into low-level if / switch /
// goto sequences, the core of Tom's pattern-matching compilation.
type Expander struct{}

func (Expander) Name() string                                 { return "Expander" }
func (Expander) Run(in platform.State) (platform.State, error) { return in, nil }

// Compiler turns expanded constraints into runnable instructions
// (or, downstream of Backend, target-language code).
type Compiler struct{}

func (Compiler) Name() string                                 { return "Compiler" }
func (Compiler) Run(in platform.State) (platform.State, error) { return in, nil }

// Optimizer applies peephole optimisations (dead-code elimination,
// branch fusion, …) to the compiled code.
type Optimizer struct{}

func (Optimizer) Name() string                                 { return "Optimizer" }
func (Optimizer) Run(in platform.State) (platform.State, error) { return in, nil }

// Backend emits the final host-language code (Java today, Go
// tomorrow). For Phase 5 we leave it as a no-op; downstream
// drivers can read State.Code directly.
type Backend struct{}

func (Backend) Name() string                                 { return "Backend" }
func (Backend) Run(in platform.State) (platform.State, error) { return in, nil }
