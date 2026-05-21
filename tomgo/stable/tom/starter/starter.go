package starter

import "tom/tomgo/stable/platform"

// Starter is the first plugin in the pipeline. Per Java's
// StarterPlugin (tom.engine.starter.StarterPlugin) it initialises
// shared state — the TomStreamManager (which holds the SymbolTable)
// — that subsequent plugins will use.
//
// In our Go port the SymbolTable lives directly on platform.State;
// Starter's sole job is to allocate an empty one if the caller did
// not pre-populate it. No transformation of the AST happens here.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Starter" }

// Run implements [platform.Plugin].
func (Plugin) Run(in platform.State) (platform.State, error) {
	out := in
	if out.Symbols == nil {
		out.Symbols = platform.NewSymbolTable()
	}
	return out, nil
}
