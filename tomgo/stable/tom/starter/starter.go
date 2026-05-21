package starter

import "tom/tomgo/stable/tom"

// Starter is the first plugin in the pipeline. Per Java's
// StarterPlugin (tom.engine.starter.StarterPlugin) it initialises
// shared state — the TomStreamManager (which holds the SymbolTable)
// — that subsequent plugins will use.
//
// In our Go port the SymbolTable lives directly on tom.State;
// Starter's sole job is to allocate an empty one if the caller did
// not pre-populate it. No transformation of the AST happens here.
func Run(in tom.State) (tom.State, error) {
	out := in
	if out.Symbols == nil {
		out.Symbols = tom.NewSymbolTable()
	}
	return out, nil
}
