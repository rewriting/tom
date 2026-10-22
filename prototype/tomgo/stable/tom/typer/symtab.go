// Package typer — SymbolTable pass.
//
// The MakeDecl bodies (parsed via `lowerActionBody` for `%op` `make`
// hooks) live in the SymbolTable side-channel rather than the
// Code tree, so the Code-walking typer doesn't reach them. This
// supplementary pass walks each symbol's options and types the
// BQApplBQTerm nodes that bodies contain — same logic as the
// in-tree `BQApplBQTerm` case (BuildTerm / BuildConsList /
// BuildConsArray), so that backquote terms in `make` hooks (e.g.
// the strategy combinators of sl.tom: `BottomUp`, `RepeatId`, …)
// resolve to the right helper calls at backend emit time.

package typer

import (
	"tom/tomgo/stable/library/sl"
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// processSymbolTableBodies walks each SymbolTomSymbol's Options for
// MakeDecl declarations and re-types any BQApplBQTerm nodes inside
// their structured bodies.
func (n *newKernelTyper) processSymbolTableBodies(st *tom.SymbolTable) {
	for name, sym := range st.Symbols {
		s, ok := sym.(*tomast.SymbolTomSymbol)
		if !ok {
			continue
		}
		opts, ok := s.Options.(*tomast.ConcOptionOptionList)
		if !ok {
			continue
		}
		newOpts := make([]tomast.Option, len(opts.Slots))
		changed := false
		for i, opt := range opts.Slots {
			declOpt, ok := opt.(*tomast.DeclarationToOptionOption)
			if !ok {
				newOpts[i] = opt
				continue
			}
			mkd, ok := declOpt.AstDeclaration.(*tomast.MakeDeclDeclaration)
			if !ok {
				newOpts[i] = opt
				continue
			}
			newInstr := n.rewriteBQApplInInstr(mkd.Instr)
			if newInstr == mkd.Instr {
				newOpts[i] = opt
				continue
			}
			newDecl := tomast.MakeMakeDecl(
				mkd.AstName, mkd.AstType, mkd.Args, newInstr, mkd.OrgTrack,
			)
			newOpts[i] = tomast.MakeDeclarationToOption(newDecl)
			changed = true
		}
		if changed {
			newSym := tomast.MakeSymbol(s.AstName, s.TypesToType, s.PairNameDeclList,
				tomast.MakeConcOption(newOpts...))
			st.Symbols[name] = newSym
		}
	}
}

// rewriteBQApplInInstr walks an Instruction and applies BQAppl
// rewriting to every embedded BQTerm. Reuses the typer's
// `inferAllTypes` driver on each BQTerm subtree.
func (n *newKernelTyper) rewriteBQApplInInstr(i tomast.Instruction) tomast.Instruction {
	intro := sl.VisitableIntrospector{}
	var walk func(any) any
	walk = func(node any) any {
		if bq, ok := node.(tomast.BQTerm); ok {
			// Drive the typer's existing BQApplBQTerm path which
			// rewrites `BQAppl(name, args)` into BuildTerm /
			// BuildConsArray / BuildConsList / FunctionCall.
			typed := n.inferAllTypes(bq, tomast.MakeEmptyType())
			if t, ok := typed.(tomast.BQTerm); ok && t != bq {
				return t
			}
			// Fall through if the typer didn't change the node —
			// still recurse into its children below.
		}
		count := intro.GetChildCount(node)
		var dup []any
		for k := 0; k < count; k++ {
			oldChild := intro.GetChildAt(node, k)
			newChild := walk(oldChild)
			if dup != nil {
				dup[k] = newChild
			} else if newChild != oldChild {
				dup = intro.GetChildren(node)
				dup[k] = newChild
			}
		}
		if dup == nil {
			return node
		}
		return intro.SetChildren(node, dup)
	}
	walked := walk(i)
	if r, ok := walked.(tomast.Instruction); ok {
		return r
	}
	return i
}
