// backend_go — thin shims over backend_shared so the rest of the
// Go backend can keep using snake_case helper names without each
// callsite knowing about backend_shared. The actual logic lives
// once in backend_shared.
//
// Only language-specific bits stay local: `makeDeclParams` chooses
// the Go param order (`name Type`) and the `any` fallback for
// unresolved TL types.

package backend_go

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/backend_shared"
)

const goFallbackTL = "any"

func flattenDeclList(d tomast.DeclarationList) []tomast.Declaration {
	return backend_shared.FlattenDeclList(d)
}

func nameString(n tomast.TomName) string { return backend_shared.NameString(n) }

func codeBodyFromExpr(e tomast.Expression) string { return backend_shared.CodeBodyFromExpr(e) }

func codeBodyFromInstruction(i tomast.Instruction) string {
	return backend_shared.CodeBodyFromInstruction(i)
}

func substitutePlaceholders(body string, names []string) string {
	return backend_shared.SubstitutePlaceholders(body, names)
}

func slotTLTypeFor(s *tomast.SymbolTomSymbol, slotName string, st *tom.SymbolTable) string {
	return backend_shared.SlotTLTypeFor(s, slotName, st, goFallbackTL)
}

func tlTypeFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	return backend_shared.TLTypeFor(s, st, goFallbackTL)
}

// makeDeclParams formats the `make` hook's BQVariable arg list as
// a Go-syntax parameter list (`name Type`).
func makeDeclParams(d *tomast.MakeDeclDeclaration, st *tom.SymbolTable) (string, []string) {
	return backend_shared.MakeDeclParams(d, st, goFallbackTL, func(name, tl string) string {
		return name + " " + tl
	})
}

func tlTypeOfBQTerm(t tomast.BQTerm, st *tom.SymbolTable) string {
	return backend_shared.TLTypeOfBQTerm(t, st, goFallbackTL)
}

func tomTypeTL(t tomast.TomType, st *tom.SymbolTable) string {
	return backend_shared.TomTypeTL(t, st, goFallbackTL)
}

func tomTypeSortName(t tomast.TomType) string { return backend_shared.TomTypeSortName(t) }

func bqTermName(t tomast.BQTerm) tomast.TomName { return backend_shared.BqTermName(t) }

func renderTomName(n tomast.TomName) string { return backend_shared.RenderTomName(n) }

func codomainSortFor(st *tom.SymbolTable, opName string) string {
	return backend_shared.CodomainSortFor(st, opName)
}

func elemTLFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	return backend_shared.ElemTLFor(s, st, goFallbackTL)
}

func codomainSortName(s *tomast.SymbolTomSymbol) string {
	return backend_shared.CodomainSortName(s)
}

func bqVarName(t tomast.BQTerm) string { return backend_shared.BqVarName(t) }

func renderTomNumber(n tomast.TomNumber) string { return backend_shared.RenderTomNumber(n) }
