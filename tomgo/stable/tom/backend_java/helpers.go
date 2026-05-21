// backend_java — thin shims over backend_shared so the rest of the
// Java backend can keep using snake_case helper names without each
// callsite knowing about backend_shared. The actual logic lives
// once in backend_shared.
//
// Only language-specific bits stay local: `makeDeclParams` chooses
// the Java param order (`Type name`) and the `Object` fallback for
// unresolved TL types; `collectUsedSymbols` uses the default helper
// prefixes shared with the Go backend.

package backend_java

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/backend_shared"
)

const javaFallbackTL = "Object"

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
	return backend_shared.SlotTLTypeFor(s, slotName, st, javaFallbackTL)
}

func tlTypeFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	return backend_shared.TLTypeFor(s, st, javaFallbackTL)
}

// makeDeclParams formats the `make` hook's BQVariable arg list as a
// Java-syntax parameter list (`Type name`).
func makeDeclParams(d *tomast.MakeDeclDeclaration, st *tom.SymbolTable) (string, []string) {
	return backend_shared.MakeDeclParams(d, st, javaFallbackTL, func(name, tl string) string {
		return tl + " " + name
	})
}

func tlTypeOfBQTerm(t tomast.BQTerm, st *tom.SymbolTable) string {
	return backend_shared.TLTypeOfBQTerm(t, st, javaFallbackTL)
}

func tomTypeTL(t tomast.TomType, st *tom.SymbolTable) string {
	return backend_shared.TomTypeTL(t, st, javaFallbackTL)
}

func tomTypeSortName(t tomast.TomType) string { return backend_shared.TomTypeSortName(t) }

func bqTermName(t tomast.BQTerm) tomast.TomName { return backend_shared.BqTermName(t) }

func renderTomName(n tomast.TomName) string { return backend_shared.RenderTomName(n) }

func codomainSortFor(st *tom.SymbolTable, opName string) string {
	return backend_shared.CodomainSortFor(st, opName)
}

func elemTLFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	return backend_shared.ElemTLFor(s, st, javaFallbackTL)
}

func codomainSortName(s *tomast.SymbolTomSymbol) string {
	return backend_shared.CodomainSortName(s)
}

func bqVarName(t tomast.BQTerm) string { return backend_shared.BqVarName(t) }

func renderTomNumber(n tomast.TomNumber) string { return backend_shared.RenderTomNumber(n) }

func collectUsedSymbols(list *tomast.ConcCodeCodeList) map[string]bool {
	return backend_shared.CollectUsedSymbols(list, backend_shared.DefaultHelperPrefixes())
}

func scanReferences(text string, used map[string]bool) {
	backend_shared.ScanReferences(text, used, backend_shared.DefaultHelperPrefixes())
}

func collectStrategySymbols(list *tomast.ConcCodeCodeList, used map[string]bool) {
	backend_shared.CollectStrategySymbols(list, used, backend_shared.DefaultHelperPrefixes())
}

func scanInstrForUsedSymbols(i tomast.Instruction, used map[string]bool) {
	backend_shared.ScanInstrForUsedSymbols(i, used, backend_shared.DefaultHelperPrefixes())
}
