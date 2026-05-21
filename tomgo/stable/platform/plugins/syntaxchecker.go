package plugins

import "tom/tomgo/stable/platform"

// SyntaxChecker is the Go port of Java's
// [tom.engine.checker.SyntaxCheckerPlugin]. Java's run() applies a
// single `TopDownCollect(CheckSyntax(this))` strategy that walks
// the AST in pure-collector mode — it accumulates diagnostics
// (unknown symbols, malformed annotations inside anti-patterns,
// invalid slot counts, …) into the platform's error stream but
// never mutates the term. AST in == AST out.
//
// We keep that semantics: the plugin walks the input (currently
// trivially via identity) and returns it unchanged. Once we surface
// a diagnostics channel in `platform.State`, the body of this
// plugin will populate it; until then the plugin is structurally a
// no-op but its presence in the pipeline pins the canonical phase
// order Parser → Transformer → SyntaxChecker → Desugarer → Typer.
type SyntaxChecker struct{}

// Name implements [platform.Plugin].
func (SyntaxChecker) Name() string { return "SyntaxChecker" }

// Run implements [platform.Plugin]. No-op walk; the AST is
// returned unchanged. Java's TomMessage diagnostics are not yet
// surfaced through [platform.State]; when they are, this is the
// place to populate them.
func (SyntaxChecker) Run(in platform.State) (platform.State, error) {
	return in, nil
}
