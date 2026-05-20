package gom

import (
	"strings"

	"tom/tomgo/stable/library/gomast"
)

// QualifiedName returns the dotted module name carried by g.
func QualifiedName(g gomast.GomModule) string {
	gm := g.(*gomast.GomModuleGomModule)
	return gm.ModuleName.(*gomast.GomModuleNameGomModuleName).Name
}

// NameParts returns the dotted module name split on dots.
func NameParts(g gomast.GomModule) []string {
	return strings.Split(QualifiedName(g), ".")
}

// CountSorts returns the number of SortType productions in g.
func CountSorts(g gomast.GomModule) int {
	n := 0
	walkProductions(g, func(p gomast.Production) {
		if _, ok := p.(*gomast.SortTypeProduction); ok {
			n++
		}
	})
	return n
}

// CountHooks returns the number of Hook productions in g.
func CountHooks(g gomast.GomModule) int {
	n := 0
	walkProductions(g, func(p gomast.Production) {
		if _, ok := p.(*gomast.HookProduction); ok {
			n++
		}
	})
	return n
}

// Sorts returns the SortType productions of g in declaration order.
func Sorts(g gomast.GomModule) []*gomast.SortTypeProduction {
	var out []*gomast.SortTypeProduction
	walkProductions(g, func(p gomast.Production) {
		if s, ok := p.(*gomast.SortTypeProduction); ok {
			out = append(out, s)
		}
	})
	return out
}

// Hooks returns the Hook productions of g in declaration order.
func Hooks(g gomast.GomModule) []*gomast.HookProduction {
	var out []*gomast.HookProduction
	walkProductions(g, func(p gomast.Production) {
		if h, ok := p.(*gomast.HookProduction); ok {
			out = append(out, h)
		}
	})
	return out
}

// Imports returns the module names listed in the optional Imports section.
func Imports(g gomast.GomModule) []string {
	gm := g.(*gomast.GomModuleGomModule)
	sectionList := gm.SectionList.(*gomast.ConcSectionSectionList)
	var out []string
	for _, sec := range sectionList.Slots {
		imp, ok := sec.(*gomast.ImportsSection)
		if !ok {
			continue
		}
		list := imp.ImportList.(*gomast.ConcImportedModuleImportList)
		for _, n := range list.Slots {
			out = append(out, n.(*gomast.GomModuleNameGomModuleName).Name)
		}
	}
	return out
}

// SortName returns the name of the sort declared by a SortType production.
func SortName(prod *gomast.SortTypeProduction) string {
	return prod.Type.(*gomast.GomTypeGomType).Name
}

// HookScope returns "sort"|"module"|"operator" depending on h.NameType.
func HookScope(h *gomast.HookProduction) string {
	switch h.NameType.(type) {
	case *gomast.KindSortIdKind:
		return "sort"
	case *gomast.KindModuleIdKind:
		return "module"
	case *gomast.KindOperatorIdKind:
		return "operator"
	}
	return ""
}

// HookKind returns the kind ("block", "make", …) of h.
func HookKind(h *gomast.HookProduction) string {
	return h.HookType.(*gomast.HookKindHookKind).Kind
}

// HookBody returns the source body of h if it is HookCode, "" otherwise.
func HookBody(h *gomast.HookProduction) string {
	if hc, ok := h.HookContent.(*gomast.HookCodeHookContent); ok {
		return hc.StringCode
	}
	return ""
}

// walkProductions walks Public-section productions of g.
func walkProductions(g gomast.GomModule, fn func(gomast.Production)) {
	gm := g.(*gomast.GomModuleGomModule)
	sectionList := gm.SectionList.(*gomast.ConcSectionSectionList)
	for _, sec := range sectionList.Slots {
		pub, ok := sec.(*gomast.PublicSection)
		if !ok {
			continue
		}
		prodList := pub.ProductionList.(*gomast.ConcProductionProductionList)
		for _, p := range prodList.Slots {
			fn(p)
		}
	}
}
