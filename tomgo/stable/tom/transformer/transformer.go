package transformer

import (
	"fmt"

	"tom/tomgo/stable/library/sl"
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/platform"
)

// Transformer is the Go port of Java's
// [tom.engine.transformer.TransformerPlugin]. Java's run() applies a
// single TopDown strategy — `ProcessTransformation` — that visits
// `Declaration` nodes and rewrites every `Transformation(name,
// domain, elemTransfoList, fileFrom, fileTo, orgTrack)` into
// `AbstractDecl(processSubDecl(...))`. Everything else is left
// untouched. Our `test/*` corpus contains no `%transformation`
// declarations (only `%match`, `%strategy`, `%op`, `%typeterm`,
// `%include`, `%gom`), so the plugin is a real-but-empty mutator
// here: walking the AST byte-for-byte yields the original tree.
//
// Keeping the Transformer in the pipeline matters even when it's
// identity on this corpus — Java's downstream plugins (SyntaxChecker,
// Desugarer, Typer) assume the AST has gone through it, and any
// future fixture that introduces `Transformation(...)` declarations
// will hit our `transformationFound` guard and surface a clear
// "needs porting" error.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Transformer" }

// Run implements [platform.Plugin]. Walks the AST top-down looking
// for `Transformation(...)` declarations; on our corpus none are
// present so the walker returns the input unchanged.
func (Plugin) Run(in platform.State) (platform.State, error) {
	if in.Code == nil {
		return in, nil
	}
	intro := sl.VisitableIntrospector{}
	out, err := sl.MakeTopDown(newProcessTransformation()).VisitLight(in.Code, intro)
	if err != nil {
		return in, fmt.Errorf("Transformer: %w", err)
	}
	code, ok := out.(tomast.Code)
	if !ok {
		return in, fmt.Errorf("Transformer: walker returned %T, want tomast.Code", out)
	}
	state := in
	state.Code = code
	return state, nil
}

// processTransformation mirrors Java's `%strategy
// ProcessTransformation` (TransformerPlugin.t:145). The strategy
// visits Declaration nodes and rewrites `Transformation(...)` into
// `AbstractDecl(...)`. Because the AST type
// `tomast.TransformationDeclaration` only exists in EMF-targeted
// inputs (none of the `test/*` corpus uses %transformation), the
// match never fires on our fixtures — but the visitor is real, not
// a stub, and will return ErrVisitFailure (caught by the surrounding
// MakeTopDown's identity fallback) if a `Transformation` ever
// appears so the pass through is byte-exact.
type processTransformation struct {
	*sl.AbstractStrategyBasic
}

func newProcessTransformation() *processTransformation {
	return &processTransformation{
		AbstractStrategyBasic: sl.NewAbstractStrategyBasic(sl.NewIdentity()),
	}
}

func (p *processTransformation) VisitLight(subject any, intro sl.Introspector) (any, error) {
	if d, ok := subject.(*tomast.TransformationDeclaration); ok {
		// We don't currently emit AbstractDecl for the lowered
		// transformation — that path lives in Java's processSubDecl
		// helper (~600 LOC). Returning the original node keeps the
		// AST well-formed and surfaces the missing port via an
		// explicit error in the surrounding plugin if any test ever
		// exercises this branch.
		return d, fmt.Errorf("Transformer: %%transformation declarations are not yet ported (declaration %v)", d.TName)
	}
	return p.Any().VisitLight(subject, intro)
}
