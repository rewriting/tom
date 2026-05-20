package plugins

import "tom/tomgo/stable/platform"

// Default returns a Platform pre-configured with the canonical Tom
// engine pipeline order (matching Java's BootstrapPluginsList):
//
//	Starter → Parser → Transformer → SyntaxChecker → Desugarer
//	      → Typer → TypeChecker → Expander → Compiler → Optimizer → Backend
//
// Phase 5: only Starter and Parser are real; the rest are identity
// pass-through stubs (see stubs.go). The chain still produces the
// correct AST in State.Code on the parser's 30 byte-equivalent
// fixtures because all subsequent stubs preserve State unchanged.
//
// Callers wanting only a partial pipeline can build their own with
// platform.New(...) directly.
func Default() *platform.Platform {
	return platform.New(
		Starter{},
		Parser{},
		Transformer{},
		SyntaxChecker{},
		Desugarer{},
		Typer{},
		TypeChecker{},
		Expander{},
		Compiler{},
		Optimizer{},
		Backend{},
	)
}
